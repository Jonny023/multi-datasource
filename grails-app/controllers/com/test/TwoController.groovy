package com.test

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class TwoController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Two.list(params), model:[twoCount: Two.count()]
    }

    def show(Two two) {
        respond two
    }

    def create() {
        respond new Two(params)
    }

    @Transactional
    def save(Two two) {
        if (two == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (two.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond two.errors, view:'create'
            return
        }

        two.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'two.label', default: 'Two'), two.id])
                redirect two
            }
            '*' { respond two, [status: CREATED] }
        }
    }

    def edit(Two two) {
        respond two
    }

    @Transactional
    def update(Two two) {
        if (two == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (two.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond two.errors, view:'edit'
            return
        }

        two.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'two.label', default: 'Two'), two.id])
                redirect two
            }
            '*'{ respond two, [status: OK] }
        }
    }

    @Transactional
    def delete(Two two) {

        if (two == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        two.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'two.label', default: 'Two'), two.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'two.label', default: 'Two'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
