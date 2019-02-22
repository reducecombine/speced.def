(ns nedap.utils.speced
  "Speced variations of clojure.core `def` forms.

  Please `:require` this namepace with the `speced` alias: `[nedap.utils.speced :as speced]`.

  That way, you will invoke e.g. `speced/defprotocol` which is clean and clear."
  (:refer-clojure :exclude [defprotocol])
  (:require
   [clojure.spec.alpha :as spec]
   [nedap.utils.spec.api :refer [check!]]
   [nedap.utils.spec.impl.defprotocol]))

(defmacro defprotocol
  "Emits a spec-backed defprotocol, which uses `nedap.utils.spec.api/check!` at runtime
  to verify that specs of return values and arguments satify the (optional) specs passed as metadata.

  Has the exact same signature as `clojure.core/defprotocol`, with the constraint that docstrings are mandatory.

  Each method name, and each argument, observes spec metadata in any of these three spec formats:

  * ^::foo            (namespace-qualified spec name with a `true` value)
  * ^{::spec ::foo}   (using a namespace-qualified ::spec, with a spec as a value)
  * ^Int              (a regular Clojure type hint, from which a spec and efficient code will be emmited)

  The implementation is backed by Clojure's `:pre`/`:post`, therefore runtime-checking behavior is controlled with `*assert*``.

  When implementing the protocol, each method must be prefixed with `--`.

  When invoking a protocol method, use the original names, without `--` prefix."
  {:style/indent [1 :defn]
   :style.cljfmt/indent [[:block 1] [:inner 1]]}
  [name docstring & methods]
  `(nedap.utils.spec.impl.defprotocol/defprotocol ~name ~docstring ~@methods))

(defmacro def-with-doc
  "Performs a plain `clojure.spec.alpha/def` with the given arguments.
  The docstring argument is omitted. Its purpose is to show up for both human readers, and tooling."
  [spec-name docstring spec]
  {:pre [(check! qualified-keyword? spec-name
                 string? docstring
                 some? spec)]}
  `(spec/def ~spec-name ~spec))
