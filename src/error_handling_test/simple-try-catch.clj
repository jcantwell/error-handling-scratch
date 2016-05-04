(ns error-handling-test.try-catch)



(defn check-for-lucky-number [n]
  (if (#{4 14 24} n)
    n
    (throw (ex-info "unlucky number" {:number n}))))


(try
  (mapv check-for-lucky-number (range 20))
  (catch RuntimeException e
    "Not much we can do to recover here."))

