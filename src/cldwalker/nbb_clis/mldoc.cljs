(ns cldwalker.nbb-clis.mldoc
  "Mldoc helpers mostly from
  https://github.com/logseq/logseq/blob/master/src/main/frontend/format/mldoc.cljs.
  Hopefully this file can be pulled from logseq one day"
  (:require ["mldoc$default" :as mldoc :refer [Mldoc]]
            ["os" :as os]
            ["fs" :as fs]
            [cljs-bean.core :as bean]
            [applied-science.js-interop :as j]))

(def parse-json* (j/get Mldoc "parseJson"))
(def ast-export-markdown* (j/get Mldoc "astExportMarkdown"))

;; TODO: Port over more of original logseq fn
(defn default-config
  []
  (->> {:toc false
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

(defn ast-export-markdown
  [ast config references]
  (str
   (ast-export-markdown*
    (-> ast clj->js js/JSON.stringify)
    config
    references)
   ;; Dunno why export swallows newline but easy to compensate
   os/EOL))

(defn file-ast [input-file config]
  (let [body (fs/readFileSync input-file)]
    (parse-json (str body) config)))
