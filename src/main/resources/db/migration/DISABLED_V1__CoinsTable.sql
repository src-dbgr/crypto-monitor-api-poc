CREATE TABLE coins (
    id UUID NOT NULL PRIMARY KEY,
    symbol VARCHAR(100) NOT NULL,
    timestmp BIGINT NOT NULL,
    datetime VARCHAR(30),
    high NUMERIC(26,10),
    low NUMERIC(26,10),
    bid NUMERIC(26,10),
    ask NUMERIC(26,10),
    vwap NUMERIC(26,10),
    close NUMERIC(26,10),
    last NUMERIC(26,10),
    basevolume NUMERIC(26,10),
    quotevolume NUMERIC(26,10),
    isymbol VARCHAR(100),
    ipricechange NUMERIC(26,10),
    ipricechangeprcnt NUMERIC(26,10),
    iweightedavgprice NUMERIC(26,10),
    iprevcloseprice NUMERIC(26,10),
    ilastprice NUMERIC(26,10),
    ilastqty NUMERIC(26,10),
    ibidprice NUMERIC(26,10),
    iaskprice NUMERIC(26,10),
    iaskqty NUMERIC(26,10),
    iopenprice NUMERIC(26,10),
    ihighprice NUMERIC(26,10),
    ilowprice NUMERIC(26,10),
    ivolume NUMERIC(26,10),
    iquotevolume NUMERIC(26,10),
    iopentime BIGINT,
    iclosetime BIGINT,
    ifirstid BIGINT,
    ilastid BIGINT,
    icount BIGINT
);