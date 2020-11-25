from pymongo import MongoClient
import pandas as pd
import numpy as np
import json

class MongoDBExecuter():
    def __init__(self, url):
        self.url = url
        self.client = MongoClient(url)

    def get_database(self, db_name):
        return self.client[db_name]

    def get_collection(self, collection, db):
        return db[collection]

    def count_documents(self, collection):
        return collection.count()

    def get_all_documents(self, collection):
        num_documents = self.count_documents(collection)
        list_docs = []
        for i in range(1, num_documents+1):
            doc = collection.find_one({"Job_id": i})
            list_docs.append(doc)
        return list_docs

    def delete_all(self, collection):
        num_documents = self.count_documents(collection)
        for i in range(1, num_documents+1):
            collection.delete_one({"Job_id": i})
        print("Total documents after delete: {}".format(self.count_documents(collection)))

if __name__ == "__main__":
    with open('mongoDB_secrets.json') as f:
        data = json.load(f)
    mdb_executer = MongoDBExecuter(data['mongoDBURL'])
    mdb_database = mdb_executer.get_database('jobs_posts')
    mdb_collection = mdb_executer.get_collection('linkedinPost', mdb_database)
    total_docs = mdb_executer.count_documents(mdb_collection)
    print("Current total documents: {}".format(total_docs))
    # mdb_executer.delete_all(mdb_collection)
    all_docs = mdb_executer.get_all_documents(mdb_collection)
    all_docs_df = pd.DataFrame(all_docs)
    print(all_docs_df.head())
