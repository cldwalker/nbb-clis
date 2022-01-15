(ns logseq-ast
  (:require ["fs" :as fs]
            ["path" :as path]
            [nbb.core]
            [cldwalker.nbb-clis.mldoc :as mldoc]))

(defn -main*
  [args]
  (let [[input] args
        config (mldoc/default-config)]
    (if (.isDirectory (fs/lstatSync input))
      (map #(hash-map :file %
                      :ast (mldoc/file-ast (path/join input %) config))
           (js->clj (fs/readdirSync input)))
      (mldoc/file-ast input config))))

(defn -main
  [args]
  (if (not= 1 (count args))
    (println "Usage: logseq-ast IN\n\nGiven a markdown"
             "IN file or directory, parses file(s) with mldoc and writes to stdout.")
    (-main* args)))

(when (= nbb.core/*file* (:file (meta #'-main)))
  (prn (-main *command-line-args*)))
