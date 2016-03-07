BIN = ./node_modules/.bin

.PHONY: test

test:
	@$(BIN)/mocha \
		--harmony-generators \
		-u bdd \
		-R spec \
		-b