(ns example.eight
  (:require [example.seven :refer [offers-for]]
            [clojure.test :refer [deftest is]]
            [clojure.spec.test.alpha :as stest]))

;; our function, but broken:
(def laser-gun {:code "LG"
                :description "A laser gun!"
                :price 100.0
                :quantity 15})

(def binoculars {:code "BB"
                 :description "A pair of binoculars."
                 :price 12.0
                 :quantity 85})

(def pet-shark {:code "PS"
                :description "A pet shark."
                :price 900.0
                :quantity 3})

(def scissors {:code "SS"
               :description "A pair of scissors."
               :price 5.0
               :quantity 4})

(def shirt {:code "DS"
            :description "A dirty shirt."
            :price 0.5
            :quantity 45})

(def warm-blanket {:code "WB"
                   :description "A warm blanket."
                   :price 7.00
                   :quantity 392})

(def samurai-sword {:code "WB"
                   :description "An ancient samurai sword."
                   :price 5000.0
                   :quantity 1})

;; we define criteria for users
(def old? #(> (:age %) 60))
(def kid? #(< (:age %) 10))
(def regular? #(= (:type %) "regular"))
(def super? #(= (:type %) "super"))
(defn a? [user]
  (when-let [code (:code user)]
    (re-matches #"[0-9]{4}" code)))

(defn b? [user]
  (when-let [code (:code user)]
    (re-matches #"[a-zA-Z0-9]{7}" code)))

;; if the user matches some criteria, they get offered a product
(def all-offers
  [old? binoculars
   super? laser-gun
   super? samurai-sword
   a? scissors
   b? pet-shark
   identity shirt
   identity warm-blanket])

(defn offers-for
  "Offers users products."
  [user]
  (->> all-offers
       (partition 2)
       (map (fn [[f offer]] (when (f user) offer)))
       (filter identity)
       (vec)))

(def mike {:name "mike"
           :type "super"
           :gender "male"
           :state "WI"
           :age 26
           :pin "1234"
           :code "A1BC5XY"})

(s/fdef offers-for
  :args (s/cat :user :domain/user)
  :ret (s/coll-of :domain/product))

(stest/instrument)

;; in example-based tests, function args will be checked
(deftest example-based
  (is (= [] (offers-for {:name "mike"
                         :type "super"
                         :state "WI"
                         :age 26
                         :pin "1234"
                         :code "A1BC5XY"}))))

(stest/check `offers-for)

(defn get-error [result]
  (-> result
      (first)
      (:clojure.spec.test.check/ret)
      :result
      (ex-data)))

(get-error (stest/check `offers-for))
