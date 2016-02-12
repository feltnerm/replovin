(ns graveyard)

(comment
  ;; drums
  ;; piano
  (defn play-chord [a-chord]
    (doseq [note a-chord] (piano/piano note)))

  (defn lil-piano-melody
    []
    (let [t (now)]
      (at t          (play-chord (chord :D3 :major7)))
      (at (+ 1000 t) (play-chord (chord :A3 :major)))
      (at (+ 2000 t) (play-chord (chord :A3 :major7)))
      (at (+ 3000 t) (play-chord (chord :F3 :major7)))))

  (definst beep [note 60]
    (let [sound-src (sin-osc (midicps note))
          env (env-gen (perc 0.01 1.0) :action FREE)]
      (* sound-src env)))

  (defsynth pad1 [freq 110 amp 1 gate 1 out-bus 0]
    (out out-bus
         (* (saw [freq (* freq 0.55)])
            (env-gen (adsr 0.01 0.1 0.7 0.5) :gate gate :action FREE))))


  ;; graveyard?
  (house-and-piano-loop (m))
  (defn house-and-piano-loop
    [beat]
    (let [next-beat (inc beat)]

      ;; kick drum
      (at (m beat)
          (drum/quick-kick :amp 0.5)
          (if (zero? (mod beat 2))
            (drum/open-hat :amp 0.1)))

      ;; clapper
      (at (m (+ 0.5 beat))
          (drum/haziti-clap :decay 1.05 :amp 0.3))

      ;; high-hat
      (when (zero? (mod beat 3))
        (at (m (+ 0.75 beat))
            (drum/soft-hat :decay 0.03 :amp 0.2)))

      ;; high-hat 2
      (when (zero? (mod beat 8))
        (at (m (+ 1.25 beat))
            (drum/soft-hat :decay 0.03)))

      ;; piano?
      (when (zero? (mod (- 1 beat) 16))
        (at (m (+ 1.25 beat))
            (lil-piano-melody)))

      ;; recurse
      (apply-by (m next-beat) #'sequencer [next-beat])
      ;; (apply-at (m (+ 4 beat)) sequencer (+ 4 beat) [])
      ))
)
