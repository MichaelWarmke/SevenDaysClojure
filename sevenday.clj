(ns simple_app.sevenday)

  (defprotocol Counter
    (increment [c])
    (currentCount [c])
    (reset [c]))

  (defrecord SimpleCounter [count]
    Counter
    (increment [_] (SimpleCounter. (+ count 1)))
    (currentCount [_] count)
    (reset [_] (SimpleCounter. 0)))

  (defn- unless-helper
    ([condition body other] (list 'if (list 'not condition) body other))
    ([condition body] (unless-helper condition body nil)))

  (defmacro unless [ & args] (apply unless-helper args))

  (defn big [st n]
    (> (count st) n))

  (defn collection-type [col]
    (cond
      (list? col) (keyword "list")
      (map? col) (keyword "map")
      (vector? col) (keyword "vector")
      (set? col) (keyword "set")))


  (defn -main
    "Here is m day one code :)"
    [& args]

    (println
      (big "eight" 8)
      (big "one" 1)
      )

    (println
      (collection-type ())
      (collection-type [])
      (collection-type #{})
      (collection-type {})
      )

    (println
      (macroexpand '(unless true "hello"))
      (macroexpand ''(body))
      (macroexpand '(unless true b1 b2))
      (macroexpand '#(count %)))

    (unless false (println "no else body unless"))
    (unless false (println "unless with an else body") "")

    (def counter (SimpleCounter. 0))
    (println (currentCount (increment counter)))

    (println (map currentCount (take 50 (iterate increment counter))))

    )