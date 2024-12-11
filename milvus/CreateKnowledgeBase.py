from pymilvus import MilvusClient, DataType

client = MilvusClient(uri="", token="")

schema = client.create_schema(enable_dynamic_field=True, description="files in knowledge base")
schema.add_field(field_name="Auto_id", datatype=DataType.INT64, description="The Primary Key", is_primary=True, auto_id=True)
schema.add_field(field_name="text_content", datatype=DataType.FLOAT_VECTOR, dim=1536)
schema.add_field(field_name="image_content", datatype=DataType.FLOAT_VECTOR, dim=512)
schema.add_field(field_name="audio_content", datatype=DataType.FLOAT_VECTOR, dim=128)
schema.add_field(field_name="video_content", datatype=DataType.FLOAT_VECTOR, dim=1024)
schema.add_field(field_name="kb_id", datatype=DataType.VARCHAR, is_partition_key=True, max_length=128)
schema.add_field(field_name="file_id", datatype=DataType.VARCHAR, max_length=128)
schema.add_field(field_name="file_part_id", datatype=DataType.VARCHAR, max_length=128)

index_params = client.prepare_index_params()
index_params.add_index(field_name="text_content", metric_type="IP", index_type="AUTOINDEX")
index_params.add_index(field_name="image_content", metric_type="L2", index_type="AUTOINDEX")
index_params.add_index(field_name="audio_content", metric_type="L2", index_type="AUTOINDEX")
index_params.add_index(field_name="video_content", metric_type="L2", index_type="AUTOINDEX")


client.create_collection(collection_name="KnowledgeBaseFiles", schema=schema, index_params=index_params)
