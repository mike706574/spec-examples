(ns domain.product
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [com.gfredericks.test.chuck.generators :as chuck-gen]))

(defmacro regex-spec [re]
  `(let [~'re-match? #(re-matches ~re %)]
     (s/spec (s/and string? ~'re-match?)
             :gen #(chuck-gen/string-from-regex ~re))))

(s/def ::code (regex-spec #"[A-Z0-9]{2}"))
(s/def ::description string?)
(s/def ::price (s/and double? pos? (partial not= ##Inf)))
(s/def ::quantity pos-int?)


(s/def :domain/product (s/keys :req-un [::code
                                        ::description
                                        ::price
                                        ::quantity]))

(s/exercise ::code)
(s/exercise ::description)
(s/exercise ::price)
(s/exercise ::quantity)
(s/exercise :domain/product)
