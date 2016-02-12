(ns live
  (:use [overtone.live])
  (:require [overtone.live :as ot]
            [overtone.inst.drum :as drum]
            [overtone.inst.synth :as synth]
            [overtone.inst.piano :as piano]
            [repl :refer [reset]]))

(println "WE'RE DOIN' IT LIVE!")

(definst hat [volume 1.0]
  (let [src (white-noise)
        env (env-gen (perc 0.001 0.3) :action FREE)]
    (* volume 1 src env)))

(defn weak-hat []
  (hat 0.3))

(defn kick []
  (drum/quick-kick :amp 0.5))

(defn phat-beats [m beat-num]
  (at (m (+ 0 beat-num)) (kick))
  (at (m (+ 0.5 beat-num)) (weak-hat))
  (at (m (+ 1.00 beat-num)) (weak-hat))
  (at (m (+ 1.50 beat-num)) (weak-hat))
  (at (m (+ 2 beat-num)) (hat))
  (at (m (+ 3 beat-num)) (weak-hat))
  (at (m (+ 3.5 beat-num)) (weak-hat))
  (at (m (+ 4 beat-num)) (kick))
  (at (m (+ 4.25 beat-num)) (weak-hat))
  (at (m (+ 5.00 beat-num)) (weak-hat))
  (at (m (+ 6 beat-num)) (hat))
  (at (m (+ 6.25 beat-num)) (weak-hat))
  (at (m (+ 6.0 beat-num)) (weak-hat))
  (at (m (+ 7.00 beat-num)) (weak-hat))
  (apply-at (m (+ 8 beat-num)) phat-beats m (+ 8 beat-num) []))

(definst dubstep [freq 100 wobble-freq 5]
  (let [sweep (lin-exp (lf-saw wobble-freq) -1 1 40 5000)
        son   (mix (saw (* freq [0.99 1 1.01])))]
    (lpf son sweep))
  )

(defn bass [m num notes]
  (at (m num)
      (ctl dubstep :freq (first notes)))
  (apply-at (m (inc num)) bass m (inc num) (next notes) [])
  )

(defn wobble [m num]
  (at (m num)
      (ctl dubstep :wobble-freq
           (choose [4 6 8 16])))
  (apply-at (m (+ 4 num)) wobble m (+ 4 num) [])
  )

(def metro (metronome 120))
(def notes (vec (map (comp midi->hz note) [:g1 :g2 :d2 :f2 :c2 :c3 :bb1 :bb2
                                           :a1 :a2 :e2 :g2 :d2 :d3 :c2 :c3])))
(comment
  (do
    (metro :bpm 180)
    (phat-beats metro (metro))
    (dubstep) ;; start the synth, so that bass and wobble can change it
    (bass metro (metro) (cycle notes))
    (wobble metro (metro))
    )
  )


(comment
  (metro-bpm metro 120)
)

(comment
  (stop)
)
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
