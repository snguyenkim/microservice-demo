#### Spring Batch - Process large volume of data

    CSV -> DB
    DB  -> CSV reports
                                                                (CSV/DB)    ------------>  (DB/CSV)
    Job Launcher    ->      Job      ->     Step        ->      ItemReader/ItemProcessor/ItemWriter
        |                         \
        |                            ->     Step        ->      ItemReader/ItemProcessor/ItemWriter
    Job Repository
        |
        (DB)





