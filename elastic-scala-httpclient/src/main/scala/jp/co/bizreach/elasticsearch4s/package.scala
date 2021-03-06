package jp.co.bizreach

import org.elasticsearch.common.geo.builders.ShapeBuilder
import org.elasticsearch.index.query._
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder

import scala.reflect.ClassTag

package object elasticsearch4s {

  def string2config(indexName: String) = ESConfig(indexName)
  def tuple2config(tuple: (String, String)) = ESConfig(tuple._1, tuple._2)

  implicit class ESStringConfig(indexName: String){
    def / (typeName: String): ESConfig = ESConfig(indexName, typeName)
  }

  def matchAllQuery = QueryBuilders.matchAllQuery
  def matchQuery(name: String, text: AnyRef) = QueryBuilders.matchQuery(name, text)
  def multiMatchQuery(text: AnyRef, fieldNames: String*) = QueryBuilders.multiMatchQuery(text, fieldNames: _*)
  def matchPhraseQuery(name: String, text: AnyRef) = QueryBuilders.matchPhraseQuery(name, text)
  def matchPhrasePrefixQuery(name: String, text: AnyRef) = QueryBuilders.matchPhrasePrefixQuery(name, text)
  def disMaxQuery = QueryBuilders.disMaxQuery
  def idsQuery(types: String*) = QueryBuilders.idsQuery(types: _*)
  def termQuery(name: String, value: String) = QueryBuilders.termQuery(name, value)
  def termQuery(name: String, value: Int) = QueryBuilders.termQuery(name, value)
  def termQuery(name: String, value: Long) = QueryBuilders.termQuery(name, value)
  def termQuery(name: String, value: Float) = QueryBuilders.termQuery(name, value)
  def termQuery(name: String, value: Double) = QueryBuilders.termQuery(name, value)
  def termQuery(name: String, value: Boolean) = QueryBuilders.termQuery(name, value)
  def termQuery(name: String, value: AnyRef) = QueryBuilders.termQuery(name, value)
  def fuzzyQuery(name: String, value: String) = QueryBuilders.fuzzyQuery(name, value)
  def fuzzyQuery(name: String, value: AnyRef) = QueryBuilders.fuzzyQuery(name, value)
  def prefixQuery(name: String, prefix: String) = QueryBuilders.prefixQuery(name, prefix)
  def rangeQuery(name: String) = QueryBuilders.rangeQuery(name)
  def wildcardQuery(name: String, query: String) = QueryBuilders.wildcardQuery(name, query)
  def regexpQuery(name: String, regexp: String) = QueryBuilders.regexpQuery(name, regexp)
  def boostingQuery = QueryBuilders.boostingQuery
  def boolQuery = QueryBuilders.boolQuery
  def spanTermQuery(name: String, value: String) = QueryBuilders.spanTermQuery(name, value)
  def spanTermQuery(name: String, value: Int) = QueryBuilders.spanTermQuery(name, value)
  def spanTermQuery(name: String, value: Long) = QueryBuilders.spanTermQuery(name, value)
  def spanTermQuery(name: String, value: Float) = QueryBuilders.spanTermQuery(name, value)
  def spanTermQuery(name: String, value: Double) = QueryBuilders.spanTermQuery(name, value)
  def spanFirstQuery(`match`: SpanQueryBuilder, end: Int) = QueryBuilders.spanFirstQuery(`match`, end)
  def spanNearQuery = QueryBuilders.spanNearQuery
  def spanNotQuery = QueryBuilders.spanNotQuery
  def spanOrQuery = QueryBuilders.spanOrQuery
  def spanMultiTermQueryBuilder(multiTermQueryBuilder: MultiTermQueryBuilder) = QueryBuilders.spanMultiTermQueryBuilder(multiTermQueryBuilder)
  def fieldMaskingSpanQuery(query: SpanQueryBuilder, field: String) = QueryBuilders.fieldMaskingSpanQuery(query, field)
  def filteredQuery(queryBuilder: QueryBuilder, filterBuilder: FilterBuilder) = QueryBuilders.filteredQuery(queryBuilder, filterBuilder)
  def constantScoreQuery(filterBuilder: FilterBuilder) = QueryBuilders.constantScoreQuery(filterBuilder)
  def constantScoreQuery(queryBuilder: QueryBuilder) = QueryBuilders.constantScoreQuery(queryBuilder)
  def functionScoreQuery(queryBuilder: QueryBuilder) = QueryBuilders.functionScoreQuery(queryBuilder)
  def functionScoreQuery = QueryBuilders.functionScoreQuery
  def functionScoreQuery(function: ScoreFunctionBuilder) = QueryBuilders.functionScoreQuery(function)
  def functionScoreQuery(queryBuilder: QueryBuilder, function: ScoreFunctionBuilder) = QueryBuilders.functionScoreQuery(queryBuilder, function)
  def functionScoreQuery(filterBuilder: FilterBuilder, function: ScoreFunctionBuilder) = QueryBuilders.functionScoreQuery(filterBuilder, function)
  def functionScoreQuery(filterBuilder: FilterBuilder) = QueryBuilders.functionScoreQuery(filterBuilder)
  def moreLikeThisQuery(fields: String*) = QueryBuilders.moreLikeThisQuery(fields: _*)
  def moreLikeThisQuery = QueryBuilders.moreLikeThisQuery
  def hasChildQuery(`type`: String, query: QueryBuilder) = QueryBuilders.hasChildQuery(`type`, query)
  def hasParentQuery(`type`: String, query: QueryBuilder) = QueryBuilders.hasParentQuery(`type`, query)
  def nestedQuery(path: String, query: QueryBuilder) = QueryBuilders.nestedQuery(path, query)
  def nestedQuery(path: String, filter: FilterBuilder) = QueryBuilders.nestedQuery(path, filter)
  def termsQuery[T](name: String, values: T*)(implicit c: ClassTag[T]) = {
    val clazz = c.runtimeClass
    if(clazz == classOf[Int]) {
      QueryBuilders.termsQuery(name, values.asInstanceOf[Seq[Int]]: _*)
    } else if(clazz == classOf[Long]){
      QueryBuilders.termsQuery(name, values.asInstanceOf[Seq[Long]]: _*)
    } else if(clazz == classOf[Float]){
      QueryBuilders.termsQuery(name, values.asInstanceOf[Seq[Float]]: _*)
    } else if(clazz == classOf[Double]){
      QueryBuilders.termsQuery(name, values.asInstanceOf[Seq[Double]]: _*)
    } else if(clazz == classOf[String]){
      QueryBuilders.termsQuery(name, values.asInstanceOf[Seq[String]]: _*)
    } else {
      QueryBuilders.termsQuery(name, values.asInstanceOf[Seq[AnyRef]]: _*)
    }
  }
  def indicesQuery(queryBuilder: QueryBuilder, indices: String*) = QueryBuilders.indicesQuery(queryBuilder, indices: _*)
  def wrapperQuery(source: String) = QueryBuilders.wrapperQuery(source)
  def wrapperQuery(source: Array[Byte], offset: Int, length: Int) = QueryBuilders.wrapperQuery(source, offset, length)
  def geoShapeQuery(name: String, shape: ShapeBuilder) = QueryBuilders.geoShapeQuery(name, shape)
  def geoShapeQuery(name: String, indexedShapeId: String, indexedShapeType: String) = QueryBuilders.geoShapeQuery(name, indexedShapeId, indexedShapeType)

}

