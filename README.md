## Description

An assortment of handy [nbb](https://github.com/babashka/nbb) CLIs.

## Prerequisites

* nodejs >= 16.3.1 and npm
* nbb >= 0.1.0 globally installed - `npm install nbb -g`

## Setup

To use these scripts/commands/executables, put `bin/` on `$PATH`:

```sh
$ git clone https://github.com/cldwalker/nbb-clis
$ export PATH=$PATH:$HOME/path/to/nbb-clis/bin
```

**Note**: No npm install step is needed as scripts automatically download their deps on their first invocation in the same way as clojure and bb scripts.

## Usage

The following scripts are under bin/:

### logseq-ast

Prints the logseq ast of a given markdown file:

```sh
$ logseq-ast example.md
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

**Note**: Experimental as there is some data loss with mldoc's export currently.

Moves blocks with specified tag to another file or directory. Useful for finely splitting a logseq knowledge graph directory.

```sh
$ logseq-block-move pages new-pages unused-tag
pages/example.md -> new-pages/example.md - 6 of 8 nodes moved
```

### logseq-roundtrip

Parses and then exports a markdown file. Useful for testing mldoc's export.

## Development

Code is organized under the following directories:
* `bin/` -  Scripts to be put on `$PATH`
* `src/` - Reusable cljs code used across scripts. Can be used as a gitlib.
* `clis/` - Contains a set of directories, each directory containing a
  package.json. A directory may contain multiple scripts as they share the same
  dependencies e.g. `clis/mldoc/`.
* `test/` - Tests for scripts


## Additional Links
* See [nbb's examples](https://github.com/babashka/nbb/tree/main/examples) which has a lot of great scripts to explore
* See https://github.com/cldwalker/bb-clis for the bb equivalent to this

## License
This repository is licensed under LICENSE.md except for [mldoc.cljs](https://github.com/cldwalker/nbb-clis/blob/main/src/cldwalker/nbb_clis/mldoc.cljs).
