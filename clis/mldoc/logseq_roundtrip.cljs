(ns logseq-roundtrip
  (:require ["fs" :as fs]
            ["path" :as path]
            [nbb.core]
            [cldwalker.nbb-clis.mldoc :as mldoc]))

(defn- roundtrip-file [input-file output-file config]
  (let [md-ast (mldoc/file-ast input-file config)]
    (fs/writeFileSync output-file
                      (mldoc/ast-export-markdown md-ast config mldoc/default-references))))

(defn -main*
  [args]
  (let [[input output] args
        config (mldoc/default-config)]
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
