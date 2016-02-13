# replovin
----

@WidenEnterprises 48Create Project with Overtone and Shadertone.

Link to performance: https://www.youtube.com/watch?v=o75qOojhlLc

## Installation

Download from http://github.com/feltnerm/replovin.

## Usage

### Emacs/CIDER

Launch a REPL!

### Lein

```
lein repl
```


## Playing Music

```clojure
user> (repl)
--> Loading Overtone...
--> Please boot a server to start making noise:
    * (boot-server)             ; boot default server (honours config)
    * (boot-internal-server)    ; boot an internal server
    * (boot-external-server)    ; boot an external server
    * (connect-external-server) ; connect to an existing external server

#namespace[repl]
repl> (play)
Starting Overtone
--> Booting internal SuperCollider server...

    _____                 __
   / __  /_  _____  _____/ /_____  ____  ___
  / / / / | / / _ \/ ___/ __/ __ \/ __ \/ _ \
 / /_/ /| |/ /  __/ /  / /_/ /_/ / / / /  __/
 \____/ |___/\___/_/   \__/\____/_/ /_/\___/

   Collaborative Programmable Music. v0.9.1


Hey Mfeltner, I feel something magical is only just beyond the horizon...

WE'RE DOIN' IT LIVE!
#namespace[live]
live> 
```

Use the commented out functions to make music! Play around and make some stuff!

## License

Copyright © 2016 Mark Feltner

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
