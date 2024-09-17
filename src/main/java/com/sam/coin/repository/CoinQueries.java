package com.sam.coin.repository;

public class CoinQueries {

    // Note Text Blocks are only available from Java 15+
    static final String INSERT_COIN = """
    INSERT INTO %s (
        id, time_stamp, symbol, coin_id,
        coin_name, price_eur, price_usd, price_btc,
        price_eth, market_cap_eur, market_cap_usd, market_cap_btc,
        market_cap_eth, total_volume_eur, total_volume_usd, total_volume_btc,
        total_volume_eth, twitter_followers, reddit_avg_posts_48_hours, reddit_avg_comments_48_hours,
        reddit_accounts_active_48_hours, reddit_subscribers, dev_forks, dev_stars,
        dev_total_issues, dev_closed_issues, dev_pull_requests_merged, dev_pull_request_contributors,
        dev_commit_count_4_weeks, dev_code_additions_4_weeks, dev_code_deletions_4_weeks, public_alexa_rank
    ) VALUES (
        ?, ?, ?, ?,
        ?, ?, ?, ?,
        ?, ?, ?, ?,
        ?, ?, ?, ?,
        ?, ?, ?, ?,
        ?, ?, ?, ?,
        ?, ?, ?, ?,
        ?, ?, ?, ?
    )""";

    static final String SELECT_ALL_COINS = """
        SELECT *
        FROM %s
        """;

    static final String SELECT_COIN_BY_ID = """
        SELECT *
        FROM %s
        WHERE id = ?
        """;

    static final String SELECT_COINS_BY_DATE_RANGE = """
        SELECT *
        FROM %s
        WHERE time_stamp BETWEEN ? AND ?
        """;

    static final String SELECT_COINS_BY_COIN_ID_AND_DATE_RANGE = """
        SELECT *
        FROM %s
        WHERE coin_id = ?
          AND time_stamp BETWEEN ? AND ?
        """;

    static final String COUNT_BY_COIN_ID = """
        SELECT COUNT(*)
        FROM %s
        WHERE coin_id = ?
        """;

    static final String DELETE_COIN_BY_ID = """
        DELETE FROM %s
        WHERE id = ?
        """;
    static final String SELECT_ALL_DUPLICATES_WITH_SAME_DATE = """
        SELECT * FROM %s WHERE DATE(time_stamp) IN (
                SELECT DATE(time_stamp) FROM %s GROUP BY DATE(time_stamp) HAVING COUNT(*) > 1
        )
        ORDER BY time_stamp DESC
        """;

    static final String DELETE_DUPLICATES_WITH_SAME_DATE = """
        DELETE FROM %s WHERE ctid NOT IN (
                SELECT DISTINCT ON (DATE(time_stamp)) ctid FROM %s ORDER BY DATE(time_stamp), ctid
        )
        """;

    static final String SELECT_LAST_VALID_DATE_FOR_COIN = """
        WITH RECURSIVE date_series AS (
                SELECT DISTINCT DATE(time_stamp) AS entry_date
                FROM %s
                ORDER BY entry_date DESC
                LIMIT 100  -- Adjust this limit if needed
        ),
        numbered_dates AS (
                SELECT
                entry_date,
                ROW_NUMBER() OVER (ORDER BY entry_date DESC) AS row_num
                FROM date_series
        ),
        sequences AS (
                SELECT
                d1.entry_date AS start_date,
                d1.row_num AS start_row,
                1 AS length
                FROM numbered_dates d1
                UNION ALL
                SELECT
                s.start_date,
                s.start_row,
                s.length + 1
                FROM sequences s
                JOIN numbered_dates d ON d.row_num = s.start_row + s.length
                WHERE d.entry_date = s.start_date - INTERVAL '1 day' * s.length
                AND s.length < %d
        )
        SELECT start_date AS last_valid_date
        FROM sequences
        WHERE length = %d
        ORDER BY start_date DESC
        LIMIT 1
        """;

    public static String getTableName(String coinId) {
        return coinId.equals("bitcoin") ? "coins" : coinId.replace("-", "_");
    }

}
