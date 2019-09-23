package com.example.demo.bean.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.example.demo.greendao.DaoSession;
import com.example.demo.greendao.OrdersDao;
import com.example.demo.greendao.CardDao;
import com.example.demo.greendao.UserDao;

@Entity(
        nameInDb = "USERS",
        indexes = {
                @Index(value = "name DESC")
        }
)
public class User {
    @Id(autoincrement = true)
    private Long id;

    private Long cardId;

    @ToOne(joinProperty ="cardId")
    private Card card;

    @ToMany(referencedJoinProperty = "userId")
    private List<Orders> orders;

    @Index(name="usercode_index",unique = true)
    private String usercode;

    @Property(nameInDb = "userName")
    @NotNull
    private String name;

    private String userAddress;

    @Transient
    private int tempUserSign;

/** Used to resolve relations */
@Generated(hash = 2040040024)
private transient DaoSession daoSession;

/** Used for active entity operations. */
@Generated(hash = 1507654846)
private transient UserDao myDao;

@Generated(hash = 231609318)
public User(Long id, Long cardId, String usercode, @NotNull String name,
        String userAddress) {
    this.id = id;
    this.cardId = cardId;
    this.usercode = usercode;
    this.name = name;
    this.userAddress = userAddress;
}

@Generated(hash = 586692638)
public User() {
}

public Long getId() {
    return this.id;
}

public void setId(Long id) {
    this.id = id;
}

public Long getCardId() {
    return this.cardId;
}

public void setCardId(Long cardId) {
    this.cardId = cardId;
}

public String getUsercode() {
    return this.usercode;
}

public void setUsercode(String usercode) {
    this.usercode = usercode;
}

public String getName() {
    return this.name;
}

public void setName(String name) {
    this.name = name;
}

public String getUserAddress() {
    return this.userAddress;
}

public void setUserAddress(String userAddress) {
    this.userAddress = userAddress;
}

@Generated(hash = 10293163)
private transient Long card__resolvedKey;

/** To-one relationship, resolved on first access. */
@Generated(hash = 2012690778)
public Card getCard() {
    Long __key = this.cardId;
    if (card__resolvedKey == null || !card__resolvedKey.equals(__key)) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        CardDao targetDao = daoSession.getCardDao();
        Card cardNew = targetDao.load(__key);
        synchronized (this) {
            card = cardNew;
            card__resolvedKey = __key;
        }
    }
    return card;
}

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 364675812)
public void setCard(Card card) {
    synchronized (this) {
        this.card = card;
        cardId = card == null ? null : card.getId();
        card__resolvedKey = cardId;
    }
}

/**
 * To-many relationship, resolved on first access (and after reset).
 * Changes to to-many relations are not persisted, make changes to the target entity.
 */
@Generated(hash = 1907478680)
public List<Orders> getOrders() {
    if (orders == null) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        OrdersDao targetDao = daoSession.getOrdersDao();
        List<Orders> ordersNew = targetDao._queryUser_Orders(id);
        synchronized (this) {
            if (orders == null) {
                orders = ordersNew;
            }
        }
    }
    return orders;
}

/** Resets a to-many relationship, making the next get call to query for a fresh result. */
@Generated(hash = 1446109810)
public synchronized void resetOrders() {
    orders = null;
}

/**
 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
 * Entity must attached to an entity context.
 */
@Generated(hash = 128553479)
public void delete() {
    if (myDao == null) {
        throw new DaoException("Entity is detached from DAO context");
    }
    myDao.delete(this);
}

/**
 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
 * Entity must attached to an entity context.
 */
@Generated(hash = 1942392019)
public void refresh() {
    if (myDao == null) {
        throw new DaoException("Entity is detached from DAO context");
    }
    myDao.refresh(this);
}

/**
 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
 * Entity must attached to an entity context.
 */
@Generated(hash = 713229351)
public void update() {
    if (myDao == null) {
        throw new DaoException("Entity is detached from DAO context");
    }
    myDao.update(this);
}

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 2059241980)
public void __setDaoSession(DaoSession daoSession) {
    this.daoSession = daoSession;
    myDao = daoSession != null ? daoSession.getUserDao() : null;
}
}