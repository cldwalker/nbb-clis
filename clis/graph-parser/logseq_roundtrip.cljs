(ns logseq-roundtrip
  "Equivalent to clis/mldoc/logseq-roundtrip but depend on logseq's graph-parser. To try this:

yarn nbb-logseq logseq_roundtrip.cljs /path/to/md-file /path/to/new-md-file"
  (:require ["fs" :as fs]
            ["path" :as path]
            [nbb.core]
            [clojure.string :as string]
            [logseq.graph-parser.mldoc :as gp-mldoc]))

(defn file-ast [input-file config]
  (let [body (fs/readFileSync input-file)]
    (gp-mldoc/->edn (str body) config)))

(defn- strip-trailing-whitespace
  "Needed b/c export has trailing whitespace bug which started somewhere b/n
  mldoc 1.3.3 and 1.5.1"
  [s]
  (string/replace s #"\s+(\n|$)" "$1"))

(defn- roundtrip-file [input-file output-file config]
  (let [md-ast (file-ast input-file config)]
    (fs/writeFileSync output-file
                      (strip-trailing-whitespace (gp-mldoc/ast-export-markdown (-> md-ast clj->js js/JSON.stringify) config gp-mldoc/default-references)))))

(defn -main*
  [args]
  (let [[input output] args
        config (gp-mldoc/default-config :markdown {:export-keep-properties? true})]
    (if (.isDirectory (fs/lstatSync input))
      (do
        (when-not (fs/existsSync output) (fs/mkdirSync output))
        (doseq [input-file (js->clj (fs/readdirSync input))]
          (roundtrip-file (path/join input input-file) (path/join output input-file) config)))
      (roundtrip-file input output config))))

(defn -main
  [args]
  (if (not= 2 (count args))
    (println "Usage: logseq-roundtrip IN OUT\n\nGiven a markdown"
             "IN file, parses and exports it with mldoc and writes to OUT file.\nIf"
             "IN is a directory, all files in directory are written to OUT directory.")
    (-main* args)))

(when (= nbb.core/*file* (:file (meta #'-main)))
  (-main *command-line-args*))
