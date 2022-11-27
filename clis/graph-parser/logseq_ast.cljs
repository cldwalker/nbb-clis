(ns logseq-ast
  "Equivalent to clis/mldoc/logseq-ast but depend on logseq's graph-parser. To try this:

yarn nbb-logseq logseq_ast.cljs /path/to/markdown-file"
  (:require ["fs" :as fs]
            ["path" :as path]
            [nbb.core]
            [clojure.pprint :as pprint]
            [logseq.graph-parser.mldoc :as gp-mldoc]))

(defn file-ast [input-file config]
  (let [body (fs/readFileSync input-file)]
    (gp-mldoc/->edn (str body) config)))

(defn -main*
  [args]
  (let [[input] args
        config (gp-mldoc/default-config :markdown)]
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
