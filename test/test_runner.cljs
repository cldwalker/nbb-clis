(ns test-runner
  (:require [clojure.test :as t]
            [nbb.core :as nbb]
            [logseq-roundtrip-test]
            [logseq-block-move-test]))

(defn init []
  (t/run-tests 'logseq-roundtrip-test
               'logseq-block-move-test))

(when (= nbb/*file* (:file (meta #'init)))
  (init))
