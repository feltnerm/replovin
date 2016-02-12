(ns live
  (:use [overtone.live])
  (:require [overtone.live :as ot]
            [overtone.inst.drum :as drum]
            [overtone.inst.synth :as synth]
            [overtone.inst.piano :as piano]
            [shadertone.tone :as t]
            [repl :refer [reset]]))

(println "WE'RE DOIN' IT LIVE!")

(defn start-shadertone
  [session-name]
  (println (apply str "Session: " session-name))
  (let [session-glsl-file (apply str "live/shaders/" session-name ".glsl") ]
    (println (apply str "Session: " session-glsl-file))
    (t/start-fullscreen session-glsl-file
                        :title session-name
                        :textures [:overtone-audio :previous-frame])))

(defn stop!
  []
  (t/stop)
  (stop))

(definst hat [volume 1.0]
  (let [src (white-noise)
        env (env-gen (perc 0.001 0.3) :action FREE)]
    (* volume 1 src env)))

(defn weak-hat []
  (hat 0.3))

(defn kick []
  (drum/quick-kick :amp 0.5))

;; (defn nerd-quote []
;;   (let [q (random-nerd-quote)]
;;     )
;;     (nerd-quote-synth)))

;; (defn nerd-quote-loop [m beat-num]
;;   (at (m (+ 0 beat-num)) (nerd-quote))
;;   (let [next-t (+ beat-num 5)]
;;     (apply-by next-t #'nerd-quote-loop [m next-t]))
;;   )

;; (nerd-quote-loop metro (metro))
;; (stop)
;; (nerd-quote)

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

(def mind-bike (load-sample "/Users/mfeltner/code/feltnerm/replovin/live/samples/mind-bike.wav"))
(def all-information (load-sample "/Users/mfeltner/code/feltnerm/replovin/live/samples/all-information.wav"))
(def tool-builders (load-sample "/Users/mfeltner/code/feltnerm/replovin/live/samples/tool-builders.wav"))
(def information-scope(load-sample "/Users/mfeltner/code/feltnerm/replovin/live/samples/information-scope.wav"))
(def nerd-quotes [mind-bike all-information tool-builders information-scope])

(defn random-nerd-quote []
  (repeatedly 2 #(rand-nth nerd-quotes)))

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

(comment
  (metro-bpm metro 180)
  )

(defsynth nerd-quote-synth []
  (let [q (random-nerd-quote)
        quote (play-buf 2 q)]
    (out 0 [quote])))

(defn nerd-quoter [m beat-num]
  (at (m (+ 0 beat-num)) (nerd-quote-synth))

  (apply-at (m (+ 8 beat-num)) nerd-quoter m (+ 8 beat-num) []) )

(comment
  (do
    (metro :bpm 180)
    (phat-beats metro (metro))
    (dubstep) ;; start the synth, so that bass and wobble can change it
    (bass metro (metro) (cycle notes))
    (wobble metro (metro))
    (nerd-quoter metro (metro))
    (nerd-quote-synth)
    )
  )

(comment
  (start-shadertone "throb")
  (t/stop)
  (start-shadertone "spectrograph")
  )

(comment
  (metro-bpm metro 100000000)
  (metro-bpm metro 80)
)

(comment
  (stop!)
)

