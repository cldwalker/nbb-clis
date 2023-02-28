## Description

An assortment of handy [nbb](https://github.com/babashka/nbb) CLIs.

## Prerequisites

* nodejs >= 16.3.1 and npm
* nbb >= 0.1.0 globally installed - `npm install nbb -g`

## Usage

For more up to date logseq clis see https://github.com/cldwalker/logseq-clis

For mldoc scripts, see [clis/mldoc](clis/mldoc).

## Development

Code is organized under the following directories:
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
