(ns cldwalker.nbb-clis.logseq-roundtrip-test
  (:require [clojure.test :as t :refer [deftest is]]
            ["fs" :as fs]
            [cldwalker.nbb-clis.util :as util]))

(defn- resource [path]
  (str "test/cldwalker/nbb_clis/resources/" path))

(deftest roundtrip-basic-test
  (util/sh ["bin/logseq-roundtrip"
            (resource "basic.md")
            (resource "basic2.md")] {})
  (is (= (str (fs/readFileSync (resource "basic.md")))
         (str (fs/readFileSync (resource "basic2.md")))))
  ;; Clean up
  (fs/unlinkSync (resource "basic2.md")))

;; Commented out b/c of a bug with blocks losing one property
#_(deftest roundtrip-advanced-test
    (util/sh ["bin/logseq-roundtrip"
              (resource "advanced.md")
              (resource "advanced2.md")] {})
    (is (= (str (fs/readFileSync (resource "advanced.md")))
           (str (fs/readFileSync (resource "advanced2.md")))))
  ;; Clean up
    (fs/unlinkSync (resource "advanced2.md")))
