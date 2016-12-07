AndroidQuery
======================

AndroidQuery is an Android SQLite and ContentProvider ORM powered by an annotation preprocessor. It focus on easy of use without sacrificing performances.

###Setup###

####Gradle dependencies####
```groovy
dependencies {
    annotationProcessor 'net.frju.androidquery:android-query-preprocessor:1.2.6'
    compile 'net.frju.androidquery:android-query:1.2.6'
}
```

If you want to use RxJava1 or RxJava2 you also need to add some of the following lines:
```groovy
    compile 'io.reactivex:rxjava:1.2.3' // For RxJava1 and rx() method
    compile 'io.reactivex:rxandroid:1.2.1' // For RxJava1 and rx() method

    compile 'io.reactivex.rxjava2:rxjava:2.0.2' // For RxJava2 and rx2() method
    compile 'io.reactivex.rxjava2:rxandroid:2.0.1' // For RxJava2 and rx2() method
```

####Initialize the ORM###
You need to initiate the ORM with a context to make it work properly. A good way to do it is by defining your Application object:
```java
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Q.init(this);
    }
}
```

And then declare it into your AndroidManifest.xml:
```xml
<application
        android:name=".App">
```

####Define your models####
You first need to declare your database. It can be a LocalDatabaseProvider (SQLite) or a ContentDatabaseProvider (ContentProvider) 
```java
public class LocalDatabaseProvider extends BaseLocalDatabaseProvider {

    public LocalDatabaseProvider(Context context) {
        super(context);
    }

    @Override
    protected String getDbName() {
        return "local_models";
    }

    @Override
    protected int getDbVersion() {
        return 1;
    }

    @Override
    protected Resolver getResolver() {
        return Q.getResolver();
    }

    @Override
    protected void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);

        // Put here your migration code
    }
}
```

Then tables are defined by POJOs that are annotated with `@Table`. Table columns are annotated with `@Column`.

```java
@Table(localDatabaseProvider = LocalDatabaseProvider.class)
public class User {
    @Column(index = true, realName = "_id", primaryKey = true, autoIncrement = true)
    public int id;
    @Column
    public String username;
    @Column
    public long timestamp;
    @Column
    public boolean isRegistered;
    @Column
    public byte[] profilePicture;
}
```

####Use custom types####

By default AndroidQuery supports several Java/Android types, but you are not restricted to them and can define some additional types:

```java
@TypeConverter(dbClass = String.class, modelClass = Uri.class)
public class UriConverter extends BaseTypeConverter<String, Uri> {

    @Override
    public String convertToDb(Uri model) {
        return model == null ? null : model.toString();
    }

    @Override
    public Uri convertFromDb(String data) {
        return data == null ? null : Uri.parse(data);
    }
}
```

###Queries###

####Q####
The Q class is generated by the annotation preprocessor, it contains a bunch of method to easily query
your data and a series of static variables that can be used to reference @Table columns. As a good practise
these variables should be used whenever you reference a table column.

####Functions####
The `insert()`, `select()`, `update()`, `save()`, `delete()`, `count()` and `raw()` methods are used to query the database tables. The `save()` method will either insert the data if not in database or will update it, since this can be slower you should use that method only if you don't know if the data has been already inserted.

If you want synchronous query, you can directly call `query()`/`querySingle()` methods or the `rx()` method.
For a `select()` query you will get back a `CursorResult` object, which needs to be closed after use. You can use a try-with-resources statement for that:

```java
// SELECT * FROM User;
try(CursorResult<User> users = Q.User.select().query()) {
    int count = users.getCount();
    User secondUser = users.get(1);
    for (User user : users) {
        //...
    }
} catch (NullPointerException e) {
    // can happen if the database returned a null cursor
}
```

You can also directly retrieve an array or a list from the `CursorResult` object.

```java
User[] usersArray = Q.User.select().query().toArray();
List<User> usersList = Q.User.select().query().toList();
```

However be careful: this is less efficient than directly using the `CursorResult` object since it needs to read and copy everything in memory.
Calling `toArray()` or `toList()` methods will automatically close the `CursorResult` object for you.

For an asynchronous query (to not block the UI), you can notably use the `rx()` method which returns an RxJava2 Observable.
It is recommended to put all the returned `Disposable` into a `CompositeDisposable` and clear it inside the activity `onDestroy()`:

```java
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private void doQuery() {
        mCompositeDisposable.add(Q.User.select()
                .rx()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CursorResult<User>>() {
                    @Override
                    public void accept(CursorResult<User> users) throws Exception {
            		// do something with the users on UI thread
                    }
                }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }
```

Other kind of queries are available:

```java
User user = new User();
user.setUsername("12345678");
user.setIsRegistered(true);
user.setTimestamp(System.currentTimeMillis());

// INSERT INTO User (username, isRegistered, timestamp) VALUES ('12345678',true,632348968244);
Q.User.insert(user).query();
```

```java
ContentValues contentValues = new ContentValues();
contentValues.put(Q.User.IS_REGISTERED, true);
contentValues.put(Q.User.TIMESTAMP, System.currentTimeMillis());

// UPDATE User SET isRegistered = 'true', timestamp = '123456789'
int rowsUpdated = Q.User.update()
        .values(contentValues)
        .query();
```

```java
// DELETE FROM User;
int rowsDeleted = Q.User.delete().query();
```

```java
// SELECT Count(*) FROM User;
int count = Q.User.count().query();
```

```java
// Raw queries;
Cursor cursor = Q.User.raw("...RAW SQL QUERY HERE...").query;
if (cursor != null) {
    // ...
    cursor.close();
}
```

####Conditions####
The `Condition` class is used to build up the where query:

```java
// SELECT * FROM User WHERE isRegistered = 'true';
User[] users = Q.User.select()
        .where(Condition.where(Q.User.IS_REGISTERED, Where.Op.IS, true))
        .query()
        .toArray();
...
users.close();
```

```java
// SELECT * FROM User WHERE username LIKE 'jo%'
User[] users = Q.User.select()
        .where(Condition.where(Q.User.USERNAME, Where.Op.LIKE, "jo%"))
        .query()
        .toArray();
...
users.close();
```

```java
// SELECT * FROM User WHERE username IN ("sam","josh");
User[] users = Q.User.select()
        .where(Condition.in(Q.User.USERNAME, "sam", "josh"))
        .query()
        .toArray();
```

```java
// SELECT * FROM User WHERE ((username = "sam" OR username = "angie") AND (timestamp >= 1234567890));
User[] users = Q.User.select()
		.where(Condition.and(
                Condition.or(
                        Condition.where(Q.User.USERNAME, Where.Op.IS, "sam"),
                        Condition.where(Q.User.USERNAME, Where.Op.IS, "angie")
                ),
                Condition.and(
                        Condition.where(Q.User.TIMESTAMP, Where.Op.MORE_THAN_OR_EQUAL_TO, 1234567890)
                )))
        .query()
        .toArray();
```

####Keywords####
The `OrderBy` and `Limit` classes are used to manipulate the results of the `select()` method

```java
// SELECT * FROM user ORDER BY username DESC
User[] users = Q.User.select()
        .orderBy(Q.User.USERNAME, OrderBy.Order.DESC)
        .query()
        .toArray();
```

```java
// SELECT * FROM user ORDER BY username DESC LIMIT 2,4
User[] users = Q.User.select()
        .limit(2,4)
        .orderBy(Q.User.USERNAME, OrderBy.Order.DESC)
        .query()
        .toArray();
```

####Joins####
Joins can be performed using the `InnerJoin`, `LeftOutJoin`, `CrossInnerJoin`, `NaturalInnerJoin`, `NaturalLeftOuterJoin` classes.
The target table for the join must be defined as an @Column, the object will be populated with any join results.

```java
@Table(localDatabaseProvider = LocalDatabaseProvider.class)
public class Comment {
    @Column(index = true)
    public int id;
    @Column
    public int userId;
    @Column
    public User user; // The target table for a potential join
}

@Table(localDatabaseProvider = LocalDatabaseProvider.class)
public class User {
    @Column(index = true)
    public int id;
}

Comment[] comments = Q.Comment.select()
                .join(innerJoin(User.class, Condition.on(Comment.class.getSimpleName() + '.' + Q.Comment.USER_ID, User.class.getSimpleName() + '.' + Q.User.ID)))
        .query().toArray();
        
User user = comments[0].getUser(); // The nested User object is populated by the join
```

####Database operation hooks####

If you need to maintain the data coherence or generate some default value you can inherits your model from `ModelListener`.

```java
@Table(localDatabaseProvider = LocalDatabaseProvider.class)
public class User implements ModelListener {

    @Column(primaryKey = true)
    public String id;
    @Column
    public long creationDate;

    @Override
    public void onPreInsert() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }

        if (creationDate == 0) {
            creationDate = System.currentTimeMillis();
        }
    }

    @Override
    public void onPreUpdate() {
    }

    @Override
    public void onPreDelete() {
    }
}
```

###ContentProvider###

Your data can also be accessed through a ContentProvider if you need to share your data across several apps or if you want to use CursorLoader or ContentObserver inside your app.

####Setup####

To do so, you first need to declare your ContentProvider:
```java
public class UserContentProvider extends BaseContentProvider {

    @Override
    protected BaseLocalDatabaseProvider getLocalSQLProvider() {
        Q.init(getContext());
        return Q.getResolver().getLocalDatabaseProviderForModel(User.class);
    }
}
```
```xml
<application
    android:name=".App">
    <provider
        android:name="net.frju.androidquery.sample.provider.UserContentProvider"
        android:authorities="net.frju.androidquery.sample.provider.UserContentProvider"
        android:exported="false" /> <!-- or true if you want to expose it to others apps -->
</application>
```

This is enough to expose your data internally or to another application via a ContentProvider. But AndroidQuery also provides a simple way to query the data from a ContentProvider:
```java
public class ContentDatabaseProvider extends BaseContentDatabaseProvider {

    public ContentDatabaseProvider(ContentResolver contentResolver) {
        super(contentResolver);
    }

    @Override
    protected String getAuthority() {
        return "net.frju.androidquery.sample.provider.UserContentProvider";
    }

    @Override
    protected Resolver getResolver() {
        return Q.getResolver();
    }
}
```

```java
@Table(localDatabaseProvider = LocalDatabaseProvider.class, contentDatabaseProvider = ContentDatabaseProvider.class)
public class User {
    @Column(primaryKey = true, realName = "_id", autoIncrement = true)
    public int id;
}
```

####Queries####

Once done, you'll be able to call the `selectViaContentProvider()`, `insertViaContentProvider()`, `updateViaContentProvider()`, `deleteViaContentProvider()` or `saveViaContentProvider()` methods directly.
```java
Q.User.insertViaContentProvider(new User()).query();
```

####Listening data changes####

To listen to the data changes, you can create a Android loader this way:
```java
public class UsersLoader extends BaseSelectLoader<User> {

    public UsersLoader(Context context) {
        super(context);
    }

    @Override
    public CursorResult<User> doSelect() {
        return User.selectViaContentProvider().query(); // It is important to not use select() here
    }
}
```
```java
public class ExampleActivity extends Activity {

    private static final int USER_LOADER_ID = 0;
    
    private final LoaderManager.LoaderCallbacks<CursorResult<User>> mLoaderCallbacks = new LoaderManager.LoaderCallbacks<CursorResult<User>>() {
    
        @Override
        public Loader<CursorResult<User>> onCreateLoader(int id, Bundle args) {
            UsersLoader loader = new UsersLoader(ExampleActivity.this);
            loader.setUpdateThrottle(100);
            return loader;
        }
    
        @Override
        public void onLoadFinished(Loader<CursorResult<User>> loader, CursorResult<User> data) {
            // ...
        }
    
        @Override
        public void onLoaderReset(Loader<CursorResult<User>> loader) {
            // ...
        }
    };
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getLoaderManager().initLoader(USER_LOADER_ID, null, mLoaderCallbacks);
    }
}
```

You can also use a simple `ContentObserver` (or a `ThrottledContentObserver` if you want to group the calls for performance reasons)
```java
getContentResolver().registerContentObserver(Q.User.getContentUri(), true, new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            // ...
        }
    });
```
```java
getContentResolver().registerContentObserver(Q.User.getContentUri(), true, new ThrottledContentObserver(new Handler(), 100) {
        @Override
        public void onChangeThrottled() {
            // ...
        }
    });
```

However, please note that raw queries and joins are not possible with a ContentProvider.

###Default Android models###

AndroidQuery also provide a library which allows you to easily access to Android data. You need to add `android-query-models` into your dependencies.
```groovy
dependencies {
    annotationProcessor 'net.frju.androidquery:android-query-preprocessor:1.2.6'
    compile 'net.frju.androidquery:android-query-models:1.2.6'
}
```

Currently the supported models are `Contact` and `BlockedNumber`.

You can queries them this way:
```java
Contact[] contacts = Q.Contact.selectViaContentProvider().query().toArray();
```

###TODO###
- Support for more constraints
- Support for Trigger
- Better default database updater (which also adds new constraints)
- Improve javadoc and add annotations like @NotNull
- Support for more types by default (ArrayList<String>, Bitmap, byte, Byte, Byte[], Set/Map, ...)
- Add more android models