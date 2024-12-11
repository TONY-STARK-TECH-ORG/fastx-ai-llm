from pymilvus import MilvusClient, DataType

client = MilvusClient(uri="", token="")

schema = client.create_schema(enable_dynamic_field=True, description="chat messages")
schema.add_field(field_name="Auto_id", datatype=DataType.INT64, description="The Primary Key", is_primary=True, auto_id=True)
schema.add_field(field_name="text_content", datatype=DataType.FLOAT_VECTOR, dim=1536)
schema.add_field(field_name="conversation_id", datatype=DataType.VARCHAR, is_partition_key=True, max_length=64)
schema.add_field(field_name="organization_id", datatype=DataType.VARCHAR, max_length=64)
schema.add_field(field_name="user_id", datatype=DataType.VARCHAR, max_length=64)
schema.add_field(field_name="message_id", datatype=DataType.VARCHAR, max_length=64)
schema.add_field(field_name="role", datatype=DataType.VARCHAR, max_length=64)
schema.add_field(field_name="create_time", datatype=DataType.VARCHAR, max_length=128)
schema.add_field(field_name="image_content", datatype=DataType.FLOAT_VECTOR, dim=512)

index_params = client.prepare_index_params()
index_params.add_index(field_name="text_content", metric_type="IP", index_type="AUTOINDEX")
index_params.add_index(field_name="image_content", metric_type="L2", index_type="AUTOINDEX")


client.create_collection(collection_name="Conversation", schema=schema, index_params=index_params)
