describe('Test e2e', () => {
  it('Check if a todo is completed', () => {

    var url = Cypress.env('URL');
    var username = Cypress.env('USERNAME');
    var password = Cypress.env('PASSWORD');

    // Definir una ruta para interceptar las solicitudes
    cy.intercept('GET', '**/count').as('apiRequests');

    cy.visit(url);

    cy.contains('a', 'Signup').click();
    cy.get('input[placeholder="Username"].form-control').type(username);
    cy.get('input[placeholder="Password"].form-control').type(password);

    cy.get('form').submit().then(() => {
      cy.wait('@apiRequests');
      cy.contains('a', 'Add Todo').click();

      cy.get('input[placeholder="Title"].form-control').type('Test Todo');
      cy.get('input[type="date"].form-control').type('2023-07-07');

      cy.get('form').submit().then(() => {
          cy.contains('a', 'View Todo').click();
          cy.get('table.table tbody tr').first().find('button.btn-success').click();
          cy.get('table.table tbody tr').first().find('button.btn-success').should('have.text', 'Mark pending');


      });

    });


  });
});