UpCatcher is created as a workaround for android app virtual memory requirements.

Use it like Shared Preferences, difference is it will convert your object to json and write down to external storage only if system memory is critical. Rest of the time it will work as a HashMap<String,Object>.

Two simple functions;

upCatcher.put(myKey,myObject);

upCatcher.get(myKey,myObject.class);
