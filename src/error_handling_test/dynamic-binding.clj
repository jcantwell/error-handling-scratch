(ns error-handling-test.dynamic-binding)



;; ==== errors ===
(defn ^:dynamic *unlucky-number-error* [msg info]
  (throw (ex-info msg info)))

;; === restart ===
(defn ^:dynamic *treat-as-lucky* [value]
  (throw (ex-info "Restart *treat-as-lucky* is unbound." {:value value})))

(defn ^:dynamic *try-again* [value]
  (throw (ex-info "Restart *try-again* is unbound." {:value value})))

(defn ^:dynamic *skip* [value]
  (throw (ex-info "Restart *skip* is unbound." {:value value})))

(defn check-for-lucky-number [n]
  (if (#{4 13 14 24} n)
    n
    (binding [*treat-as-lucky* identity
              *try-again* (fn [value]
                            (println "trying again with "value)
                            (check-for-lucky-number value))
              *skip* (fn [] "-")]
       (*unlucky-number-error*
          "Landed on unlucky number"
          {:number n}))))


(defn am-I-lucky? [n]
;;outer function chooses what restart to call based on the error
   (binding [*unlucky-number-error*
            (fn [msg info]
              (*try-again* (rand-int 10)))]
     (if (check-for-lucky-number n) "YES")))

(am-I-lucky? 41)

;;Without binding a restart all we just know something went wrong
(try
  (mapv check-for-lucky-number (range 20))
  (catch RuntimeException e
    "Unlucky number in the sequence"))

;;Using restarts we can handle the error appropriately at
;;the point it occurred
(defn lucky-numbers? []
  (binding [*unlucky-number-error*
            (fn [msg info]
              (*skip*))]
    (mapv check-for-lucky-number (range 20))))

(lucky-numbers?)
