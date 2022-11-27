;; Copyright (C) 2022 Logseq under GPL 3.0
;; This file is a modified version of https://github.com/logseq/logseq/blob/master/src/main/frontend/format/mldoc.cljs
(ns cldwalker.nbb-clis.mldoc
  "Mldoc helpers mostly from
  https://github.com/logseq/logseq/blob/master/src/main/frontend/format/mldoc.cljs.
  Hopefully this file can be pulled from logseq one day"
  (:require ["mldoc$default" :as mldoc :refer [Mldoc]]
            ["fs" :as fs]
            [cljs-bean.core :as bean]
            [applied-science.js-interop :as j]))

(def parse-json* (j/get Mldoc "parseJson"))
(def ast-export-markdown* (j/get Mldoc "astExportMarkdown"))

;; TODO: Port over more of original logseq fn
(defn default-config
  []
  (->> {:toc false
        :parse_outline_only false
        :heading_number false
        :keep_line_break true
        :format :Markdown
        :heading_to_list false
        :exporting_keep_properties true
        :export_md_indent_style "dashes"}
       (bean/->js)
       (js/JSON.stringify)))

(def default-references
  (js/JSON.stringify
   (clj->js {:embed_blocks []
             :embed_pages []})))

(defn- json->clj
  [json-string]
  (-> json-string
      (js/JSON.parse)
      (js->clj :keywordize-keys true)))

(def parse-json
  (comp json->clj parse-json*))

;; Sometimes newline at end of file is chopped on export. Maybe newline
;; detection could help
(defn ast-export-markdown
  [ast config references]
  (ast-export-markdown*
   (-> ast clj->js js/JSON.stringify)
   config
   references))

(defn file-ast [input-file config]
  (let [body (fs/readFileSync input-file)]
    (parse-json (str body) config)))
