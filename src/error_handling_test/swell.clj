(ns error-handling-test.condition-system
  (:require [slingshot.core :refer [throw+]]
            [swell.api :refer [restart-case handler-bind invoke-restart]]))



(defn check-for-lucky-number [n]
  (if (#{4 14 24} n)
    n
    (restart-case
      [
        :try-again (fn [value]
                     (println "trying again with "value)
                     (check-for-lucky-number value))
        :treat-as-lucky (fn []
                          (println "treating the value as lucky")
                          n)
        :skip (fn []
                (println "Returning dash instead")
                "-")
        ]
      (throw+ {:type ::unlucky-number :number n} "Landed on unlucky number"))
    ))



(defn lucky-number? [n]
  (handler-bind
    [#(= ::unlucky-number (:type %))
     (fn [e]
       ;;choose the restart appropriate for the error
       ;; could also rethrow error
       (invoke-restart
         :try-again (rand-int 10)
         ))]
       (if (check-for-lucky-number n) "YES")))


;;try with an unlucky number - instead of error being raised the check-for-lucky-number
;;function is recalled with different values until it finds a lucky number
(lucky-number? 16)

;;Without binding a restart all we just know something went wrong
(try
  (mapv check-for-lucky-number (range 20))
  (catch RuntimeException e
    "Unlucky number in the sequence"))


;;Using restarts we can handle the error appropriately at the point it occurred
(defn lucky-numbers? []
  (handler-bind
    [#(= ::unlucky-number (:type %))
     (fn [e]
       (invoke-restart
         :skip
         ))]
    (mapv check-for-lucky-number (range 20))))

(lucky-numbers?)

