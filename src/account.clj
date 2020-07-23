(ns simple-app.account)

(defprotocol Account
  (debit [_ amount])
  (credit [_ amount]))

; right now we will assume that the input to these functions are bigdec
(defrecord CashAccount [ balance ]
  Account
  (debit [_ amount] (CashAccount. (bigdec (- balance amount))))
  (credit [_ amount] (CashAccount. (bigdec (+ balance amount)))))


(defn -main [& args]

  (def accounts
    (take 2 (repeat (ref (CashAccount. (bigdec 0))))))

  (println accounts)
  (println "accounts: " (map (fn [x] @x) accounts))
  (println (credit @(first accounts) (bigdec 1000)))

  (dosync
    (def accounts
      (map (fn [x] (ref (credit @x 1000))) accounts)))

  (println "accounts: " (map deref accounts))

  (dosync
    (dorun
      (map #(alter % credit 100) accounts)))

  (println "accounts: " (map deref accounts))

  )