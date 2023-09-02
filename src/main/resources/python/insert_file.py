import psycopg2
from sentence_transformers import SentenceTransformer
import numpy as np
from psycopg2.extensions import register_adapter, AsIs
import configparser
config = configparser.ConfigParser()
config.read('insert_file.properties')
# Access the properties
host = config['database']['host']
dbname = config['database']['dbname']
user = config['database']['user']
password = config['database']['password']
port = config['database']['port']
path = config['file']['path']

print(host, dbname, user, password, port, path)


# Load a pre-trained model
model = SentenceTransformer('paraphrase-MiniLM-L6-v2')

# Generate embeddings for a list of sentences
sentences = ["This is an example sentence", "Embeddings are dense vectors"]
embeddings = model.encode(sentences)

#print(embeddings)
# Check the shape of the embedding
dimensions = embeddings.shape[1]
print(f"The dimensions of the vector is: {dimensions}")



def addapt_numpy_array(numpy_array):
    return AsIs(tuple(numpy_array))

def addapt_numpy_float64(numpy_float64):
    return AsIs(numpy_float64)

def addapt_numpy_int64(numpy_int64):
    return AsIs(numpy_int64)

def addapt_numpy_float32(numpy_float32):
    return AsIs(numpy_float32)

def addapt_numpy_int32(numpy_int32):
    return AsIs(numpy_int32)

def addapt_numpy_array(numpy_array):
    return AsIs(tuple(numpy_array))

register_adapter(np.float64, addapt_numpy_float64)
register_adapter(np.int64, addapt_numpy_int64)
register_adapter(np.float32, addapt_numpy_float32)
register_adapter(np.int32, addapt_numpy_int32)
register_adapter(np.ndarray, addapt_numpy_array)



try:
    # Establish the connection
    conn = psycopg2.connect(
        dbname=dbname,
        user=user,
        password=password,
        host=host,
        port=port
    )

    # Create a new cursor
    cur = conn.cursor()

    # Execute a simple query
    cur.execute("SELECT version();")
    version = cur.fetchone()
    print(f"Connected to - {version[0]}")
    cur.execute("""
        CREATE TABLE IF NOT EXISTS sentence_embeddings (
            id SERIAL, 
            sentence text,
            embedding vector(384)
        );
    """)
    for sentence, embedding in zip(sentences, embeddings):
        str_arr = ','.join(map(str, embedding.ravel()))
        print(f"Embedding String: {str_arr}")
        cur.execute(f"INSERT INTO sentence_embeddings (sentence, embedding) VALUES ('{sentence}', '[{str_arr}]');")

    print("inserted data....")
    cur.execute('commit')

    # Close the cursor and connection
except psycopg2.Error as e:
    print(f"Error: {e}")
finally:
    if cur:
        cur.close()
    if conn:
        conn.close()