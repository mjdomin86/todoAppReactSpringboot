describe('Test e2e', () => {
  it('Check if a todo is completed', () => {
    var url = Cypress.env('URL');
    var username = Cypress.env('USERNAME');
    var password = Cypress.env('PASSWORD');
    cy.visit(url);
    cy.get('.nav-link:nth-child(2) > a').click();
    cy.get('.form-group:nth-child(2) > .form-control').click();
    cy.get('.form-group:nth-child(2) > .form-control').type(username);
    cy.get('.form-group:nth-child(3) > .form-control').click();
    cy.get('.form-group:nth-child(3) > .form-control').type(password);
    cy.get('.btn').click();
    cy.get('form').submit();
    cy.get('.nav-link:nth-child(2) > a').click();
    cy.visit(url);
    cy.get('.nav-link:nth-child(3) > a').click();
    cy.get('.form-group:nth-child(2) > .form-control').click();
    cy.get('.form-group:nth-child(2) > .form-control').type('Test 1');
    cy.get('.form-group:nth-child(3) > .form-control').click();
    cy.get('.form-group:nth-child(3) > .form-control').type('2023-07-04');
    cy.get('.btn').click();
    cy.get('form').submit();
    cy.get('.nav-link:nth-child(2) > a').click();
    cy.get('tr:nth-child(1) .btn-success').click();
    cy.get('tr:nth-child(1) .btn-success').should('have.text', 'Mark pending');

  })
})