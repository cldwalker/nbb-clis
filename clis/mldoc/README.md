## Description

Demonstrates how to use nbb with https://github.com/logseq/mldoc.

## Usage

### logseq-ast

Prints the logseq ast of a given markdown file:

```sh
$ nbb -cp src clis/mldoc/logseq_ast.cljs example.md
[[["Heading"
   {:anchor "apple",
    :level 1,
    :meta {:properties [], :timestamps []},
    :size nil,
    :tags [],
    :title [["Plain" "apple "] ["Tag" [["Plain" "some-tag"]]]],
    :unordered true}]
  {:end_pos 18, :start_pos 0}]
 [["Heading"
   {:anchor "banana",
    :level 2,
    :meta {:properties [], :timestamps []},
    :size nil,
    :tags [],
    :title [["Plain" "banana "]
            ["Tag" [["Plain" "work"]]]
            ["Plain" " "]
            ["Tag" [["Plain" "blarg"]]]],
    :unordered true}]
  {:end_pos 41, :start_pos 18}]
...
```

### logseq-block-move

Moves blocks with specified tag to another file or directory. Useful for finely splitting a logseq knowledge graph directory.

```sh
$ nbb -cp src clis/mldoc/logseq_block_move.cljs pages new-pages unused-tag
pages/example.md -> new-pages/example.md - 6 of 8 nodes moved
```
