(ns logseq-block-move-test
  (:require [clojure.test :as t :refer [deftest is]]
            ["fs" :as fs]
            [cldwalker.nbb-clis.util :as util]))

(defn- resource [path]
  (str "test/resources/" path))

(deftest block-move-test-with-valid-tag
  ;; Setup
  (fs/copyFileSync (resource "advanced.md") (resource "block-move.md"))

  (util/sh ["bin/logseq-block-move"
            (resource "block-move.md")
            (resource "block-move-moved.md")
            "work"] {})
  (is (= (str (fs/readFileSync (resource "block-move.md")))
         (str (fs/readFileSync (resource "block-move-keep.md"))))
      "Input file has specified tag blocks removed")

  (is (= (str (fs/readFileSync (resource "block-move-moved.md")))
         ;; TODO: Update this file once export bug with missing property is fixed
         (str (fs/readFileSync (resource "block-move-remove.md"))))
      (str "Output file has specified tag blocks moved to it. Blocks have been"
           " unindented to top level."))

  ;; Clean up
  (fs/unlinkSync (resource "block-move.md"))
  (fs/unlinkSync (resource "block-move-moved.md")))
