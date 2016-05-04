(ns error-handling-test.monads
  (require [cats.core :as m])
  (require [cats.builtin])
  (require [cats.monad.either :as either])
  (require [cats.monad.maybe :as maybe]))


(defn value-set [value]
  (if (nil? value)
    (either/left "Required value")
    (either/right value)))


(defn valid-email [value]
  (if (re-matches #"\S+@\S+\.\S+" value)
     (either/right value)
     (either/left "invalid email")))

(defn valid-zip-code [zipCode]
  (if (re-matches #"\d{5}" zipCode)
      (either/right zipCode)
      (either/left "invalid zip code")))

(let [contact {:name "batman"
               :email "batman99@gmail.com"
               :zipCode "12345"}]
  (pr-str
    (m/mlet [ valueSet (value-set (:name contact))
              email (valid-email (:email contact))
              zip (valid-zip-code (:zipCode contact))]
            contact)))

(let [contact {:name nil
               :email "batman99@gmail.com"
               :zipCode "12345678"}]
  (pr-str
    (m/mlet [ valueSet (value-set (:name contact))
              email (valid-email (:email contact))
              zip (valid-zip-code (:zipCode contact))]
            contact)))










