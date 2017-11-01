(ns example.five
  (:require [clojure.spec.alpha :as s]
            [com.gfredericks.test.chuck.generators :as chuck-gen]))

;; so with `predicates`, `composition`, and `naming`...

(defmacro regex-spec [re]
  `(let [~'re-match? #(re-matches ~re %)]
     (s/spec (s/and string? ~'re-match?)
             :gen #(chuck-gen/string-from-regex ~re))))

(def states #{"WI" "SC" "MN" "NV" "NM" "NE" "AK" "NH" "ME" "NY" "TN" "FL" "IA"
              "GA" "IL" "RI" "VA" "MI" "PA" "UT" "WY" "SD" "MO" "KY" "CT" "AR"
              "ID" "DC" "MA" "OK" "AL" "VT" "MS" "CA" "LA" "DE" "WA" "KS" "MD"
              "ND" "TX" "OR" "NC" "AZ" "IN" "WV" "CO" "HI" "MT" "NJ" "OH"})

(s/def :domain.user/name (s/and string? #(<= 4 (count %) 16)))
(s/def :domain.user/type #{"super" "regular"})
(s/def :domain.user/age (s/int-in 0 126))
(s/def :domain.user/gender #{"male" "female"})
(s/def :domain.user/state states)
(s/def :domain.user/pin #(re-matches #"[0-9]{4}" %))
(s/def :domain.user/a-code #(partial re-matches #"[0-9]{5}" %))
(s/def :domain.user/b-code #(partial re-matches #"[A-Z0-9]{7}" %))
(s/def :domain.user/code (s/or :nil nil?
                               :a :domain.user/a-code
                               :b :domain.user/b-code))

(s/def :domain/user (s/keys :req-un [:domain.user/name
                                     :domain.user/type
                                     :domain.user/age
                                     :domain.user/gender
                                     :domain.user/state
                                     :domain.user/pin
                                     :domain.user/code]))

(comment
  (s/exercise :domain/user)
  )

;; we've defined `specifically` what a `user` is in our `domain`

(def mike {:name "mike"
           :type "super"
           :gender "male"
           :state "WI"
           :age 26
           :pin "1234"
           :code "A1BC5XY"})

(s/valid? :domain/user mike)
;; => true

(def bob {:name "bob"
          :type "hardcore"
          :age -5
          :code "21"})

(s/valid? :domain/user bob)
;; => false

(s/explain-str :domain/user bob)
;; => string explanation

(s/explain-data :domain/user bob)
;; => data explanation
