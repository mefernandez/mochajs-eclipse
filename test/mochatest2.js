var assert = require('assert');

describe('Another Mocha Test in /test folder', function() {

	it("with a test that fails", function(done) {
		assert.ok(false);
		done();
	});

	it("with a test that passes", function(done) {
		assert.ok(true);
		done();
	});
	
});