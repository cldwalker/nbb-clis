(ns logseq-block-move
  "WIP - Like logseq-block-move but loses properties so don't use yet"
  (:require ["fs" :as fs]
            ["path" :as path]
            ["util" :as util]
            [nbb.core]
            [logseq.graph-parser.mldoc :as gp-mldoc]))

(defn- handle-heading-node
  [acc attr node f]
  (cond
    (f attr)
    (-> acc
        (assoc :remove-level (:level attr)
               :last-operation :remove)
        (update :remove conj node))

    ;; Remove children of ast node
    (some-> (:remove-level acc) (< (:level attr)))
    (-> acc
        (assoc :last-operation :remove)
        (update :remove conj node))

    :else
    (-> acc
        (dissoc :remove-level)
        (update :keep conj node)
        (assoc :last-operation :keep))))

(defn- remove-by-fn
  "Splits given markdown ast based on f and returns a map of :keep and :remove
  ast nodes. When f evaluates to true, ast node is put in :remove and otherwise
  node is put in :keep. Non-heading nodes like property_drawer are placed
  wherever their heading is."
  [f ast]
  (reduce
   (fn [acc [[node-type attr] _position :as node]]
     ;; This assumes Heading node is the only one that sets a level.
     ;; There are a number of node types that belong to the previous header node
     ;; including Paragraph, Property_Drawer and Timestamp
     (if (not= "Heading" node-type)
       (if (= :keep (:last-operation acc))
         (-> acc
             (update :keep conj node)
             (assoc :last-operation :keep))
         (-> acc
             (update :remove conj node)
             (assoc :last-operation :remove)))
       (handle-heading-node acc attr node f)))
   {:keep [] :remove []}
   ast))

(defn file-ast [input-file config]
  (let [body (fs/readFileSync input-file)]
    (gp-mldoc/->edn (str body) config)))

(defn move-input-to-output [input output config f]
  (let [md-ast (file-ast input config)
        {:keys [keep remove]} (remove-by-fn f md-ast)]
    (if (empty? remove)
      (println (util/format "%s -> %s - Did not occur as there are 0 nodes to move"
                            input output))
      (println (util/format "%s -> %s - %s of %s nodes moved"
                            input output (count remove) (+ (count keep) (count remove)))))
    ; (prn (gp-mldoc/ast-export-markdown keep config gp-mldoc/default-references))
    ; (prn :KEEP keep)
    (if (empty? keep)
      (fs/rmSync input)
      (fs/writeFileSync input
                        (gp-mldoc/ast-export-markdown (-> keep clj->js js/JSON.stringify) config gp-mldoc/default-references)))
    (prn :RM remove)
    (when (seq remove)
      (fs/writeFileSync output
                        (gp-mldoc/ast-export-markdown (-> remove clj->js js/JSON.stringify) config gp-mldoc/default-references)))))

(defn -main*
  [args]
  (let [[input output tag] args
        config (gp-mldoc/default-config :markdown)
        remove-f #(contains? (set (:title %)) ["Tag" [["Plain" tag]]])]
    (if (.isDirectory (fs/lstatSync input))
      (do
        (when-not (fs/existsSync output) (fs/mkdirSync output))
        (doseq [input-file (js->clj (fs/readdirSync input))]
          (move-input-to-output (path/join input input-file)
                                (path/join output input-file)
                                config
                                remove-f)))
      (move-input-to-output input output config remove-f))))

(defn -main
  [args]
  (if (not= 3 (count args))
    (println "Usage: logseq-block-move IN OUT TAG\n\nMoves blocks and their children"
             "tagged with TAG from IN file to OUT file.\nIf IN is a directory,"
             "all files in directory have operation done and written to OUT directory.")
    (-main* args)))

(when (= nbb.core/*file* (:file (meta #'-main)))
  (-main *command-line-args*))
