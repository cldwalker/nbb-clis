(ns logseq-ast
  "Equivalent to clis/mldoc/logseq-ast but depend on upstream
logseq ns. To try this:

bb nbb logseq_ast.cljs /path/to/markdown-file"
  (:require ["fs" :as fs]
            ["path" :as path]
            [nbb.core]
            [clojure.pprint :as pprint]
            [logseq.graph-parser.util :as gp-util]
            [logseq.graph-parser.mldoc :as mldoc]))

(defn file-ast [input-file config]
  (let [body (fs/readFileSync input-file)]
    ;; Could also just call mldoc/->edn
    (-> (str body)
        (mldoc/parse-json config)
        gp-util/json->clj)))

(defn -main*
  [args]
  (let [[input] args
        config (mldoc/default-config :markdown)]
    (if (.isDirectory (fs/lstatSync input))
      (map #(hash-map :file (path/join input %)
                      :ast (file-ast (path/join input %) config))
           (js->clj (fs/readdirSync input)))
      (file-ast input config))))

(defn -main
  [args]
  (if (not= 1 (count args))
    (println "Usage: logseq-ast IN\n\nGiven a markdown"
             "IN file or directory, parses file(s) with mldoc and writes to stdout.")
    (-main* args)))

(when (= nbb.core/*file* (:file (meta #'-main)))
  (pprint/pprint (-main *command-line-args*)))
