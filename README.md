# helmikuu

A [re-frame](https://github.com/Day8/re-frame) application that has a simple
frontend for Wordpress blog.

## Development Mode
You need to start a worpress instance and assume your posts are found in
localhost.
Also, you need to add your wordpress posts links to config.cljs (if they differ
from http://localhost:8000). Also make sure, your wordpress url structure is
"Post Name" i.e. http://localhost:8000/sample-post/ You can change this in
Settings -> General --> Permalink Settings

In scripts-folder you can find and example docker-compose file that you can use
to set up testing environment. 

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

After this you will have the app in /resources/public.