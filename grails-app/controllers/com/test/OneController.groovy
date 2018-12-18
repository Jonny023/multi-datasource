package com.test

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class OneController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond One.list(params), model:[oneCount: One.count()]
    }

    def show(One one) {
        respond one
    }

    def create() {
        respond new One(params)
    }

    @Transactional
    def save(One one) {
        if (one == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (one.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond one.errors, view:'create'
            return
        }

        one.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'one.label', default: 'One'), one.id])
                redirect one
            }
            '*' { respond one, [status: CREATED] }
        }
    }

    def edit(One one) {
        respond one
    }

    @Transactional
    def update(One one) {
        if (one == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (one.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond one.errors, view:'edit'
            return
        }

        one.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'one.label', default: 'One'), one.id])
                redirect one
            }
            '*'{ respond one, [status: OK] }
        }
    }

    @Transactional
    def delete(One one) {

        if (one == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        one.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'one.label', default: 'One'), one.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'one.label', default: 'One'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
