(ns example.seven
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]))

;; now with our specs in `domain.user` and `domain.product`,
;;   let's do something in our `domain`

;; we have a bunch of products
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

;; (setq cider-show-error-buffer 't)

(comment
  ;; first, let's use our function `without` spec...
  (offers-for)
  ;; => 1. Unhandled clojure.lang.ArityException
  ;;        Wrong number of args (0) passed to: six/offers-for

  ;; `spec` the function
  (s/fdef offers-for
    :args (s/cat :user :domain/user)
    :ret (s/coll-of :domain/product))

  ;; view the `docs`
  offers-for

  ;; `instrument` the namespace
  (stest/instrument)

  ;; now let's use our function `with` spec...
  (offers-for)
  ;; => Spec assertion failed.

  (offers-for 434)
  ;; => Spec assertion failed.

  (offers-for {:name "fred"
               :type "extreme"
               :age 45
               :code "ABCDE"})
  ;; => Spec assertion failed.;

  (def mike {:name "mike"
             :type "super"
             :gender "male"
             :state "WI"
             :age 26
             :pin "1234"
             :code "A1BC5XY"})

  (offers-for mike)
  ;; => Offers!

  (offers-for mike 12)
  ;; => Spec assertion failed.
  )
