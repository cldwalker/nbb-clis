(ns cldwalker.nbb-clis.test-runner
  (:require [clojure.test :as t]
            [nbb.core :as nbb]
            [cldwalker.nbb-clis.logseq-roundtrip-test]))

(defn init []
  (t/run-tests 'cldwalker.nbb-clis.logseq-roundtrip-test))

(when (= nbb/*file* (:file (meta #'init)))
  (init))
