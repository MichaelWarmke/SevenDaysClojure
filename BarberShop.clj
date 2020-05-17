(ns simple-app.BarberShop)

; i have no clue why this npe
(defn pushCustomer [ queue customer suppliedCount]
  (let [derefQue @queue]
    (if (< (count derefQue) 3)
     (dosync
       (do
        (alter suppliedCount inc)
        (alter queue conj customer))))))

(defn consumeCustomer [ queue consumedCount]
  (if (not-empty @queue)
    (dosync
      (println "getting next customer:" (peek @queue))
      (alter consumedCount inc)
      (println "new wait list" (alter queue pop)))))

(defn supplyCustomers [queue suppliedCount isActive]
  (println "Starting Supplier")
  (while @isActive
     (pushCustomer queue (rand-int 100) suppliedCount)
    (Thread/sleep (+ 10 (rand-int 20)))))


(defn consumeCustomers [queue consumedCount isActive]
  (println "Starting Consumer")
  (while @isActive
    (consumeCustomer queue consumedCount)
    (Thread/sleep 20)))

(defn -main [& args]
  (dosync
    (def waitingChairs (ref []))
    (def suppliedCount (ref 0))
    (def consumedCount (ref 0))
    (def isActive (ref true)))

  ;(pushCustomer waitingChairs "Customer" suppliedCount)
  ;(consumeCustomer waitingChairs consumedCount)
  ;(println @consumedCount)
  ;(println @suppliedCount))

  (future (supplyCustomers waitingChairs suppliedCount isActive))
  (Thread/sleep 1)
  (future (consumeCustomers waitingChairs consumedCount isActive))

  (Thread/sleep 10000)

  (dosync
    (def isActive (ref false)))

  (Thread/sleep 50)

  (println "SuppliedCount: " @suppliedCount)
  (println "ConsumedCount: " @consumedCount)

  (System/exit 0))


