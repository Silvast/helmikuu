# helmikuu

A [re-frame](https://github.com/Day8/re-frame) application that has a simple
frontend for Wordpress blog.

## Development Mode
You need to start a worpress instance and assume your posts are found in
localhost. So, add your worpress postlink to config.edn.

### Run application:

```
lein clean
lein figwheel dev
```

Figwheel will automatically push cljs changes to the browser.

Wait a bit, then browse to [http://localhost:3449](http://localhost:3449).

## Production Build


To compile clojurescript to javascript:

```
lein clean
lein cljsbuild once min
```
