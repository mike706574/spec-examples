(ns domain.user
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [com.gfredericks.test.chuck.generators :as chuck-gen]))

(defmacro regex-spec [re]
  `(let [~'re-match? #(re-matches ~re %)]
     (s/spec (s/and string? ~'re-match?)
             :gen #(chuck-gen/string-from-regex ~re))))

(def states #{"WI" "SC" "MN" "NV" "NM" "NE" "AK" "NH" "ME" "NY" "TN" "FL" "IA"
              "GA" "IL" "RI" "VA" "MI" "PA" "UT" "WY" "SD" "MO" "KY" "CT" "AR"
              "ID" "DC" "MA" "OK" "AL" "VT" "MS" "CA" "LA" "DE" "WA" "KS" "MD"
              "ND" "TX" "OR" "NC" "AZ" "IN" "WV" "CO" "HI" "MT" "NJ" "OH"})

(s/def ::name (s/and string? #(<= 4 (count %) 16)))
(s/def ::type #{"super" "regular"})
(s/def ::age (s/int-in 0 126))
(s/def ::gender #{"male" "female"})
(s/def ::state states)
(s/def ::pin (regex-spec #"[0-9]{4}"))
(s/def ::a-code (regex-spec #"[0-9]{5}"))
(s/def ::b-code (regex-spec #"[A-Z0-9]{7}"))
(s/def ::code (s/or :nil nil?
                    :a ::a-code
                    :b ::b-code))

(s/def :domain/user (s/keys :req-un [::name
                                     ::type
                                     ::age
                                     ::gender
                                     ::state
                                     ::pin
                                     ::code]))

(s/exercise ::name)
(s/exercise ::type)
(s/exercise ::age)
(s/exercise ::gender)
(s/exercise ::state)
(s/exercise ::pin)
(s/exercise ::a-code)
(s/exercise ::b-code)
(s/exercise ::code)
(s/exercise :domain/user)
