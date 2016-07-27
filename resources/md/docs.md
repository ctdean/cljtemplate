
# Overview

`iter` is a way of iterating and looping in Clojure. It provides an
alternate to using the traditional Clojure tools such as higher order
functions and threading macros.  `iter` provides a rich set of
operators to walk a sequence, return a lazy sequence, return a single
expression, or reduce a complex sequence.

Let's walk through a simple example:

    (iter (foreach x [1 2 3 4 5])
          (collect (+ x 100)))

      => (101 102 103 104 105)

The `iter` macro contains a mix of iter operators and regular Clojure
expressions.  In this example,

- `foreach` binds the variable `x` in turn to each element in the
  Clojure sequence `[1 2 3 4 5]`.
- The variable `x` is now in scope for the rest of the `iter` form.
- The `collect` operator adds the result of the regular Clojure
  expression `(+ x 100)` to the lazy sequence that `iter` returns.
- The lazy sequence `(101 102 103 104 105)` is returned.

Iter has a rich set of operators so you can perform more complex
processing.  Let's look at some more examples, the first with a simple
`if` branch::

    (iter (foreach x [3 1 4 1 5 9 2 6 5 3 5 8])
          (if (even? x)
              (collect (* x x))))

      => (16 4 36 64)

and with an if/else operator:

    (iter (foreach x (range 10))
          (if (even? x)
              (collect (+ 10 x))
              (collect (int (Math/pow 2 x)))))

      => (10 2 12 8 14 32 16 128 18 512)

and with multiple collects:

    (iter (foreach num (range 5))
          (collect (* num num))
          (when (even? num)
            (collect (+ 10 num))))

      => (0 10 1 4 12 9 16 14)

It's also easy to build up a hash-map:

    (iter (foreach word ["apple" "peaches" "pumpkin pie"])
          (collect-map word (count word)))

      => {"pumpkin pie" 11, "peaches" 7, "apple" 5}

Or a unique sequence:

    (iter (foreach x [3 1 4 1 5 9 2 6 5 3 5 8])
          (collect-uniq (+ x 10)))

      => (13 11 14 15 19 12 16 18)

You can also reduce a sequence:

    (iter (foreach x (range 10))
          (when (odd? x)
            (reducing x :by +)))

      => 25

Or bind new variables within the scope of iter:

    (iter (foreach x (range 10))
          (let [y (* x x)]
            (when (> y 20)
              (collect y))))

      => (25 36 49 64 81)

Or maximize a value:

    (iter (foreach x [3 1 4 1 5 9 2 6 5 3 5 8])
          (maximizing x))

      => 9

    (iter (foreach s (clojure.string/split
                      "Lisp is a programmable programming language" #" "))
          (maximizing s :using (count s)))

      => "programmable"

In general, the `iter` macro contains a series of forms, where some of
the forms are `iter` operators and the rest of the forms are just
plain Clojure expressions.
