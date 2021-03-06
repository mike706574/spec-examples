(ns example.six
  (:require [clojure.spec.alpha :as s]
            [clojure.string :as str]
            [clojure.spec.gen.alpha :as gen]
            [clojure.spec.test.alpha :as stest]
            [com.gfredericks.test.chuck.generators :as chuck-gen]))

;; (setq cider-show-error-buffer nil)

;; one problem: spec can't generate values for arbitrary predicates
(s/def ::foo-code #(str/starts-with? % "foo-"))

(s/valid? ::foo-code "foo-902 kdx$Bb")
;; => true

(s/gen ::foo-code)
;; => ExceptionInfo Unable to construct gen at: [] for: :example.six/foo-code

;; but there are generators for primitive types, so being more specific helps
(s/def ::foo-code (s/and string? #(str/starts-with? % "foo-")))

(s/gen ::foo-code)
;; => {:gen #function[clojure.test.check.generators/such-that/fn--11463]}

;; but it's still extremely unlikely that we can generate a correct value
(gen/sample (s/gen ::foo-code))
;; => ExceptionInfo Couldn't satisfy such-that predicate after 100 tries.

;; but we can substitute a simpler generator
(s/def ::foo-code (s/spec (s/and string? #(str/starts-with? % "foo-"))
                          :gen #(s/gen #{"foo-90 2kdx$Bb" "foo-24#0e#@d" "foo-AB43_EW3"})))

(gen/sample (s/gen ::foo-code))

;; or, with test.check, a better random generator:
(s/def ::foo-code (s/spec (s/and string? #(str/starts-with? % "foo-"))
                          :gen #(gen/fmap (fn [string] (str "foo-" string))
                                          (gen/string))))

(gen/sample (s/gen ::foo-code))
;; => ("foo-"
;;     "foo-."
;;     "foo-á"
;;     "foo-kØ5"
;;     "foo-rÉ"
;;     "foo-"
;;     "foo-Ø+!ó"
;;     "foo-¹(ô« Â"
;;     "foo-jßJôÓâ"
;;     "foo-;s\fSZ")


;; or we can use a handy library, like test.chuck:
(s/def ::pin #(re-matches #"[0-9]{4}" %))

(gen/sample (s/gen ::pin))

(s/def ::pin (s/spec (s/and string? #(re-matches #"[0-9]{4}" %))
                     :gen #(chuck-gen/string-from-regex #"[0-9]{4}")))

(gen/sample (s/gen ::pin))
