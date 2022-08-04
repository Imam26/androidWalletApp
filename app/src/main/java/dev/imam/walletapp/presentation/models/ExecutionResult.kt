package dev.imam.walletapp.presentation.models

enum class ResultType {
    Success, Error, Loading
}

class ExecutionResult<T> {
    var resultType: ResultType? = null
    var message: String = ""
    var data: T? = null

    companion object {
        fun <T> success(data: T): ExecutionResult<T> {
            val result = ExecutionResult<T>()
            result.data = data
            result.resultType = ResultType.Success
            return result
        }

        fun <T> error(message: String): ExecutionResult<T> {
            val result = ExecutionResult<T>()
            result.message = message
            result.resultType = ResultType.Error
            return result
        }
        
        fun <T> loading() : ExecutionResult<T> {
            val result = ExecutionResult<T>()
            result.resultType = ResultType.Loading
            return result
        }
    }
}