(ns cldwalker.nbb-clis.util
  (:require ["path" :as path]
            ["child_process" :as child-process]
            [nbb.core]
            ["fs" :as fs]))

(defn sh
  "Run shell cmd synchronously and print to inherited streams by default. Aims
  to be similar to babashka.tasks/shell"
  [cmd opts]
  (child-process/spawnSync (first cmd)
                           (clj->js (rest cmd))
                           (clj->js (merge {:stdio "inherit"} opts))))

(defn ensure-node-modules
  "Downloads npm packages if they don't exist"
  [cli-dir]
  ;; Could be nice to make this check as intelligent as babashka.fs/modified-since
  (when-not (fs/existsSync (path/join cli-dir "node_modules"))
    (sh ["npm" "install"] {:cwd cli-dir})
    (println)))
