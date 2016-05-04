(ns error-handling-test.monads
  (require [cats.core :as m])
  (require [cats.builtin])
;;   (require [clojure.string/upper-case :as upper])
  (require [cats.applicative.validation :as v]))

;;returning a special value to signal errors nil/maybe/either

(defn value-set [value]
  (if (nil? value)
    (v/fail {:required "Required value"})
    (v/ok value)))


(defn valid-email [value]
  (if (re-matches #"\S+@\S+\.\S+" value)
     (v/ok value)
     (v/fail {:email "invalid email"})))

(defn valid-zip-code [zipCode]
  (if (re-matches #"\d{5}" zipCode)
      (v/ok zipCode)
      (v/fail {:zipCode "invalid zip code"})))


(let [contact {:name "batman" :email "batman99@gmail.com" :zipCode "12345"}]
  (pr-str
    (m/alet [ valueSet (value-set (:name contact))
              email (valid-email (:email contact))
              zip (valid-zip-code (:zipCode contact))]
            contact)))


  (let [contact {:name nil :email "batman99@gmail.com" :zipCode "123456"}]
    (pr-str
      (m/alet [ valueSet (value-set (:name contact))
                email (valid-email (:email contact))
                zip (valid-zip-code (:zipCode contact))]
              contact)))




(m/extract (m/fmap clojure.string/upper-case (value-set nil)))




