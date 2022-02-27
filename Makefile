.DEFAULT_GOAL := help

##                                                                            .
## ============================================================================
## DBSeeder - Relational Database Data Generator - make Documentation.
##       ---------------------------------------------------------------
##       The purpose of this Makefile is to support the software
##       development process for DBSeeder. it contains also the necessary
##       tools for the CI activities.
##       ---------------------------------------------------------------
##       The available make commands are:
## ----------------------------------------------------------------------------
## help:               Show this help.
## ----------------------------------------------------------------------------
## docs:               Create and upload the user docs.
## ----------------------------------------------------------------------------

help:
	@sed -ne '/@sed/!s/## //p' $(MAKEFILE_LIST)

# Project documentation with Markdown.
# https://github.com/mkdocs/mkdocs/
# Configuration file: none
mkdocs:             ## Create and upload the user documentation with MkDocs.
	@echo "Info **********  Start: MkDocs **************************************"
	python -m pip install --upgrade pip
	python -m pip install --upgrade pipenv
	python -m pipenv install --dev
	python -m pipenv update --dev
	pipenv run pip freeze
	python --version
	python -m pip --version
	pipenv run mkdocs --version
	pipenv run mkdocs gh-deploy --force
	@echo "Info **********  End:   MkDocs **************************************"

## ============================================================================
