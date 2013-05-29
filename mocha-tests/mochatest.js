var assert = require('assert');

describe('A Mocha Test in /mocha-tests folder', function() {

	it("with a test that fails", function(done) {
		assert.ok(false);
		done();
	});

	it("with a test that passes", function(done) {
		assert.ok(true);
		done();
	});
	
});